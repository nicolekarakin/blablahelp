import {Box, Typography} from "@mui/material";

import React from "react";
import NotOwnOfferType from "../../../types/NotOwnOfferType";
import MatchedOffer from "./MatchedOffer";
import AddressType from "../../../types/AddressType";

type MatchedOffersProp = {
    offers: NotOwnOfferType[],
    mitshopperAddress: AddressType,
}
export default function MatchedOffers(props: MatchedOffersProp) {


    return (
        <Box>
            <Typography variant={'h3'} ml={2} mr={2} mt={2}>
                Suchergebnisse für
            </Typography>
            <Typography ml={2} mr={2} variant="h5" component="div" color="text.secondary" gutterBottom>
                {props.mitshopperAddress.street}, {props.mitshopperAddress.zip} {props.mitshopperAddress.city}
            </Typography>

            {
                props.offers.map((a, index) => {
                    return (<MatchedOffer key={index} selectedOffer={a}
                                          mitshopperAddress={props.mitshopperAddress}/>)
                })
            }
        </Box>
    )
}
