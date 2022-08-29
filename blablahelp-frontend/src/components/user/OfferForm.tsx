import React, {FormEvent, useContext, useEffect, useState} from "react";
import {AuthContext} from "../../shared/AuthProvider";
import {

    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    FormControl, FormControlLabel, FormLabel,
    Grid, InputLabel, MenuItem, Radio, RadioGroup, Select,
    Stack,
    TextField, Typography
} from "@mui/material";

import {useSnackbar} from "notistack";
import OwnOfferType from "../../types/OwnOfferType";
import AddressType from "../../types/AddressType";
import DateTimeOfferFormPart from "./DateTimeOfferFormPart";

import DynamicTextInput from "./DynamicTextInput";
import useOfferForm from "./useOfferForm";


export default function OfferForm(){
    const {currentUser, setCurrentUser,currentCountry} = useContext(AuthContext);
    const {getShopNames, getShopAddresses} = useOfferForm()
    const [shoppingDate, setShoppingDate] = useState<Date | null>(null);
    const [timeFrom, setTimeFrom] = useState<Date | null>(null);
    const [timeTo, setTimeTo] = useState<Date | null>(null);

    const [shopCity, setShopCity] = useState<string | undefined>(undefined);
    const [shopname, setShopname] = useState<string | undefined>(undefined);

    const [shopAddress, setShopAddress] = useState<AddressType | null>(null);
    const [destinationAddress, setDestinationAddress] = useState<AddressType>({
        city: "", country: currentCountry, street: "", zip:"", type:"PRIVATE"
    });
    const [useSaved,setUseSaved]=useState<string| undefined>(undefined);

    const [maxMitshoppers, setMaxMitshoppers] = useState<number | null>(null);
    const [maxDrinks, setMaxDrinks] = useState<number | null>(null);
    const [maxArticles, setMaxArticles] = useState<number | null>(null);
    const [maxDistanceKm, setMaxDistanceKm] = useState<number | null>(null);

    const [agbAccepted,setAgbAccepted] = useState<boolean>(false);
    const [priceOffer,setPriceOffer] = useState<string | undefined>(undefined);

    const {enqueueSnackbar} = useSnackbar();

    useEffect(()=>{
        console.log("shopname "+shopname)
        console.log( (!!shopname && shopname.length > 0))

        if(shopname && shopCity && shopAddress && shopAddress.street && shopAddress.street.length > 2){

        }
    },[shopname,shopCity])



    const handleNewOfferSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        const newOfferData : OwnOfferType = {
            id: undefined,
            accountId: currentUser.id,
            date: shoppingDate?.getTime(),
            timeFrom: timeFrom?.getTime(),
            timeTo: timeTo?.getTime(),
            city:shopCity,
            shopname:shopname,
            shopAddress:shopAddress,

            destinationAddress:destinationAddress,
            maxMitshoppers:maxMitshoppers,
            maxDrinks:maxDrinks,
            maxArticles:maxArticles,
            maxDistanceKm:maxDistanceKm,
            agbAccepted: agbAccepted,
            priceOffer: priceOffer,
        }
        console.log("newOfferData "+ JSON.stringify(newOfferData))

        // axios
        //     .post(urls.BASIC[0]+"/"+currentUser.id+ "/"+urls.BASIC[1], newOfferData)
        //     .then(response => {
        //         const ownOfferData:OwnOfferType = response?.data
        //         setCurrentUser((currentUser:userType) => ({
        //             ...currentUser,
        //             ownOffers: [...(currentUser?.ownOffers ?? []), ownOfferData]
        //         }));
        //
        //     })
        //     .catch(_ => {
        //         enqueueSnackbar('New Offer Submission Failed', {variant: "error"})
        //     });
    }

    const getUsedPrivateAddresses=()=>{
        return currentUser.userData.usedAddresses
            .filter((a:AddressType)=>a.type==="PRIVATE")
    }

    return (

                <Card elevation={3}>
                    <form onSubmit={handleNewOfferSubmit}>
                    <CardHeader title={"Neues Angebot erstellen"}/>
                    <CardContent>

                            <Stack spacing={2}>

                                    <TextField
                                        required
                                        variant="outlined"
                                        placeholder={"Deine Stadt"}
                                        value={shopCity ?? ""}
                                        autoComplete={"username"}
                                        onChange={e => setShopCity(e.target.value)}
                                        fullWidth
                                        error={false}
                                        label="Stadt wo du einkaufst"
                                        helperText=""
                                    />

                                <DynamicTextInput
                                    label={"Shop Brand"}
                                    required={true}
                                    disabled={false}
                                    getData={getShopNames}
                                    helperText=""
                                    axiosProps={{}}
                                    noOptionsText={"Leider keine Option verfügbar"}
                                    setSelectValue={setShopname}
                                    selectValue={shopname}
                                />
                                {
                                    (!!shopname && shopname.length > 0)?
                                        <DynamicTextInput
                                            label={"Shop Adresse"}
                                            required={true}
                                            disabled={false}
                                            getData={getShopAddresses}
                                            setSelectValue={setShopAddress}
                                            selectValue={shopAddress}
                                            helperText=""
                                            axiosProps={{city:shopCity,shopname:shopname}}
                                            noOptionsText={"Leider kein Shop mit dieser Adresse vorhanden"}
                                        />
                                        :<></>
                                }

                                <Typography component={'p'}  sx={{fontSize: "1.2rem"}}>Endzieladresse</Typography>

                                {
                                    (!!currentUser?.userData
                                        && !!currentUser?.userData.usedAddresses
                                        && getUsedPrivateAddresses().length > 0)?
                                        <FormControl>
                                            <FormLabel id="useSaved-radio-buttons-group-label">
                                                Geben Sie Ihr Endziel</FormLabel>
                                            <RadioGroup aria-labelledby="useSaved-radio-buttons-group-label"
                                                        value={useSaved}
                                                        onChange={(e)=>setUseSaved(e.target.value)}
                                            >
                                                <FormControlLabel value={"true"} control={<Radio />} label="Gespeicherte Adressen" />
                                                <FormControlLabel value={"false"} control={<Radio />} label="Neue Adresse" />
                                            </RadioGroup>
                                        </FormControl>
                                        :
                                        <></>
                                }

                                {
                                    (useSaved==="true")?

                                        <FormControl fullWidth>
                                            <InputLabel id={"usedBefore"}>Ihre zuvor verwendeten Adressen</InputLabel>
                                            <Select
                                                required
                                                labelId="usedBefore"
                                                value={ JSON.stringify(destinationAddress) || ""}
                                                label="Ihre zuvor verwendeten Adressen"

                                                onChange={(e)=> {
                                                    setDestinationAddress(JSON.parse(e.target.value))
                                                }}
                                            >
                                                {
                                                    getUsedPrivateAddresses().map((address:AddressType,index: React.Key | null | undefined) =>
                                                            <MenuItem value={JSON.stringify(address)} key={index}>
                                                                {address.street+", "+address.zip+" "+address.city}
                                                            </MenuItem>

                                                    )
                                                }
                                            </Select>
                                        </FormControl>
                                        :<></>
                                }

                                {
                                    (useSaved==="false" || useSaved===undefined)?
                                        <Grid container >
                                            <Grid item md={8} xs={12}>
                                                <TextField
                                                    fullWidth
                                                    required
                                                    variant="outlined"
                                                    placeholder={"Stadt"}
                                                    defaultValue={shopCity ?? ""}
                                                    value={destinationAddress?.city ?? ""}
                                                    onChange={e => {
                                                        setDestinationAddress({ ...destinationAddress, city: e.target.value })
                                                        }
                                                    }
                                                    label="Stadt"
                                                />
                                            </Grid>
                                            <Grid item md={4} xs={12}>
                                                <TextField
                                                    fullWidth
                                                    required
                                                    variant="outlined"
                                                    placeholder={"Postleitzahl"}
                                                    value={destinationAddress?.zip ?? ""}
                                                    onChange={e => {
                                                        setDestinationAddress({ ...destinationAddress, zip: e.target.value })
                                                    }
                                                    }
                                                    label="Postleitzahl"
                                                />
                                            </Grid>
                                            <Grid item md={12} xs={12} mt={"1.2rem"}>
                                                <TextField
                                                    fullWidth
                                                    required
                                                    variant="outlined"
                                                    placeholder={"Straße und Hausnummer"}
                                                    value={destinationAddress?.street ?? ""}
                                                    onChange={e => {
                                                        setDestinationAddress({ ...destinationAddress, street: e.target.value })
                                                    }
                                                    }
                                                    label="Straße und Hausnummer"
                                                />
                                            </Grid>
                                        </Grid>
                                        :
                                        <></>
                                }
                                <DateTimeOfferFormPart
                                    setShoppingDate={setShoppingDate}
                                    shoppingDate={shoppingDate}
                                    setTimeFrom={setTimeFrom}
                                    timeFrom={timeFrom}
                                    setTimeTo={setTimeTo}
                                    timeTo={timeTo}
                                />
                            </Stack>

                    </CardContent>
                    <CardActions sx={{padding:'0 1rem 1.5rem 1rem'}}>
                        <Button variant={"contained"}
                                color={"primary"}
                                type="submit"
                        >
                            Erstellen</Button>

                    </CardActions>
                    </form>
                </Card>
    )
}
