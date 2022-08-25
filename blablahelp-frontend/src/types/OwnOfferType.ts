import AddressType from "./AddressType";

type OwnOfferType = {
    id?: string,
    date: number | undefined,
    timeFrom: number | undefined,
    timeTo: number | undefined,
    city:string | undefined,
    shopname:string | undefined,
    shopAddress:AddressType | null,
    destinationAddress:AddressType |null,
    maxMitshoppers:number|null,
    maxDrinks:number|null,
    maxArticles:number|null,

}
export default OwnOfferType
