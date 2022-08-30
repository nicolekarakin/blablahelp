import axios from "axios";
import {urls} from "../shared/UrlMapping";
import AddressType from "../types/AddressType";
import {useContext} from "react";
import {AuthContext} from "../shared/AuthProvider";

type T = { title: string, item: any }

export default function useOfferForm() {
    //TODO for addresses, check their geolocation, if null get it with mapbox and save in db

    const {currentUser, currentCountry, currentLang} = useContext(AuthContext);


    const getShopNames = () => {
        return axios
            .get(urls.BASIC[0] + '/' + currentCountry + urls.SHOP[0])
            .then(response => {
                const dataOb: { shopnames: string[] } = response.data
                const result: T[] = []
                dataOb.shopnames.forEach(a => result.push({title: a, item: a}))
                return result
            })
    }

    const getShopAddresses = (props:{shopname:string, city:string,search?:string})=>{
        const locale=currentLang.concat("-",currentCountry)
        let params:any
        if(props.search)
            params = new URLSearchParams([['city', props.city],['search',props.search]]);
        else
            params = new URLSearchParams([['city', props.city]]);
        return axios
            .get(urls.BASIC[0] + '/'+locale+urls.SHOP[1]+"/"+props.shopname+"/addresses", {params})
            .then(response => {
                const dataOb:AddressType[]=response.data
                const result:T[]=[]
                dataOb.forEach(a => result.push({title: a.street + ", " + a.zip + " " + a.city, item: a}))
                return result
            })
    }

    return {
        getShopNames,
        getShopAddresses,
    }
}
