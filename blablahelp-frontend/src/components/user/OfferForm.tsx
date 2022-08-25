import React, {FormEvent, useContext, useState} from "react";
import {AuthContext} from "../../shared/AuthProvider";
import {Button, Card, CardActions, CardContent, CardHeader, Container, Grid, Stack, TextField} from "@mui/material";
import axios from "axios";

import {useSnackbar} from "notistack";
import OwnOfferType from "../../types/OwnOfferType";
import AddressType from "../../types/AddressType";
import DateTimeOfferFormPart from "./DateTimeOfferFormPart";


export default function OfferForm(){
    const {currentUser, setCurrentUser} = useContext(AuthContext);
    const [shoppingDate, setShoppingDate] = useState<Date | null>(null);
    const [timeFrom, setTimeFrom] = useState<Date | null>(null);
    const [timeTo, setTimeTo] = useState<Date | null>(null);

    const [city, setCity] = useState<string | undefined>(undefined);
    const [shopname, setShopname] = useState<string | undefined>(undefined);

    const [shopAddress, setShopAddress] = useState<AddressType | null>(null);
    const [destinationAddress, setDestinationAddress] = useState<AddressType | null>(null);

    const [maxMitshoppers, setMaxMitshoppers] = useState<number | null>(null);
    const [maxDrinks, setMaxDrinks] = useState<number | null>(null);
    const [maxArticles, setMaxArticles] = useState<number | null>(null);

    const {enqueueSnackbar} = useSnackbar();

    const handleNewOfferSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        const newOfferData : OwnOfferType = {
            id: undefined,
            date: shoppingDate?.getTime(),
            timeFrom: timeFrom?.getTime(),
            timeTo: timeTo?.getTime(),
            city:city,
            shopname:shopname,
            shopAddress:shopAddress,
            destinationAddress:destinationAddress,
            maxMitshoppers:maxMitshoppers,
            maxDrinks:maxDrinks,
            maxArticles:maxArticles,
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
                                        value={city ?? ""}
                                        autoComplete={"username"}
                                        onChange={e => setCity(e.target.value)}
                                        fullWidth
                                        error={false}
                                        label="Ort"
                                        helperText="Stadt wo du einkaufst"
                                    />


                                    <TextField
                                        required
                                        variant="outlined"
                                        placeholder={"Shop Brand"}
                                        type={"text"}
                                        value={shopname ?? ""}
                                        autoComplete={"current-password"}
                                        onChange={e => setShopname(e.target.value)}
                                        fullWidth
                                        error={false}
                                        label="Shop Brand"
                                        helperText="Shop Brand (z.B. Lidl)"
                                    />
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
