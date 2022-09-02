import OwnOfferType from "../../types/OwnOfferType";
import {Box, List, Typography} from "@mui/material";
import CurrentOwnOffer from "./CurrentOwnOffer";
import React from "react";

type CurrentOwnOfferListProps={
    offers:OwnOfferType[],
}

export default function CurrentOwnOfferList(props:CurrentOwnOfferListProps){

    return (
        <Box>
            <Typography variant={'h1'} ml={2} mr={2}>
                Aktuelle Angebote
            </Typography>
            <List sx={{ width: '100%', bgcolor: 'background.paper', paddingBottom:"1rem" }}  >
            {
                props.offers.map((a, index) => (
                    <CurrentOwnOffer key={index} o={a} offerLength={props.offers.length} keyIndex={index}/>
                ))
            }
            </List>
        </Box>
    )
}
