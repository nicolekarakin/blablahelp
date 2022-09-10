import {Box, CircularProgress, Typography} from "@mui/material";
import CurrentPublicOffer from "./CurrentPublicOffer";
import axios from "axios";
import {urls} from "../../shared/UrlMapping";
import {useSnackbar} from "notistack";
import React, {useEffect, useState} from "react";
import CurrentPublicOfferType from "../../types/CurrentPublicOfferType";

export default function CurrentPublicOffers(){
    const [loading, setLoading] = useState(false);
    const [currentOffers, setCurrentOffers] = useState<CurrentPublicOfferType[]>([]);
    const {enqueueSnackbar} = useSnackbar();

    const getPublicOffers = () => {
        return axios
            .get(urls.PUBLIC[0] + urls.PUBLIC[1])
            .then(response => {
                const offers: CurrentPublicOfferType[] = response.data
                setCurrentOffers([...offers])

            })
            .catch(_ => {
                enqueueSnackbar('Failed to load current offers', {variant: "error"})
            });
    }

    useEffect(() => {
        setLoading(true);
        getPublicOffers()
            .finally(() => {
                setLoading(false)
            });
    }, []);

    return (
        <>
            {loading && (
                <CircularProgress/>
            )}
        {
            (currentOffers && currentOffers.length > 0) ?
            <Box>
                <Typography variant={'h1'} ml={2} mr={2}>
                    Aktuelle Angebote
                </Typography>
                {
                    currentOffers.map((a, index) => {
                        return (<CurrentPublicOffer key={index} {...a}/>)
                    })
                }
            </Box>
            : <></>
        }
        </>
    )
}
