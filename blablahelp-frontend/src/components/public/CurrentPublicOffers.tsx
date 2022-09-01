import {Box, CircularProgress, Typography} from "@mui/material";
import CurrentPublicOffer, {CurrentPublicOfferProp} from "./CurrentPublicOffer";
import axios from "axios";
import {urls} from "../../shared/UrlMapping";
import {useSnackbar} from "notistack";
import React, {useEffect, useState} from "react";

// const fakeData=[
//     {
//     name: "Agneschka",
//     date: 1663022076000,
//     motto: "well meaning and kindly",
//     shopname:"Lidl",
//     city:"Ulm",
//     rating:4,
// },
//     {
//         name: "Bernd",
//         date: 1663454491000,
//         motto: "Jede Woche eine neue Welt",
//         shopname:"Lidl",
//         city:"Hannover",
//         rating:2,
//     },
// ]

export default function CurrentPublicOffers(){
    const [loading, setLoading] = useState(false);
    const [currentOffer, setCurrentOffer] = useState<CurrentPublicOfferProp[]>([]);
    const {enqueueSnackbar} = useSnackbar();

    const getPublicOffers = () => {
        return axios
            .get(urls.PUBLIC[0] + urls.PUBLIC[1])
            .then(response => {
                const offers:CurrentPublicOfferProp[] = response.data
                setCurrentOffer(offers)
            })
            .catch(_ => {
                enqueueSnackbar('Failed to load current offers', {variant: "error"})
            });
    }

    useEffect(() => {
        setLoading(true);
        getPublicOffers().finally(() => setLoading(false));
    }, []);

    return (
        <>
            {loading && (
                <CircularProgress/>
            )}
        {
            (currentOffer && currentOffer.length > 0) ?
            <Box>
                <Typography variant={'h1'} ml={2} mr={2}>
                    Aktuelle Angebote
                </Typography>
                {
                    currentOffer.map((a, index) => (<CurrentPublicOffer key={index} {...a}/>))
                }
            </Box>
            : <></>
        }
        </>
    )
}
