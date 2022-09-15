import OwnOfferType from "../../types/OwnOfferType";
import {Box, List, Typography} from "@mui/material";
import CurrentOwnOffer from "./CurrentOwnOffer";
import React, {useContext} from "react";
import {AuthContext} from "../../shared/AuthProvider";

type CurrentOwnOfferListProps = {
    offers: OwnOfferType[],
}

export default function CurrentOwnOfferList(props: CurrentOwnOfferListProps) {
    const {currentUser} = useContext(AuthContext);
    if (currentUser.currentOffers && currentUser.currentOffers.length > 0) {
        return (
            <Box>
                <Typography variant={'h1'} ml={2} mr={2}>
                    Shopping
                </Typography>
                <List
                    sx={{width: '100%', bgcolor: 'background.paper', paddingBottom: "1rem"}}>
                    {
                        props.offers.map((a, index) => (
                            <CurrentOwnOffer key={index} o={a} offerLength={props.offers.length} keyIndex={index}/>
                        ))
                    }
                </List>
            </Box>
        )
    } else return <></>
}
