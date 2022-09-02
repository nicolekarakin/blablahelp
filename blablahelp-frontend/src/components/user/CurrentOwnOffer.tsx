import {Button, Divider, ListItem, ListItemAvatar, ListItemText, Typography} from "@mui/material";
import React, {useContext} from "react";
import OwnOfferType from "../../types/OwnOfferType";
import {addressToString, getShortMonthString, timeFromInstant} from "../../shared/util";
import {AuthContext} from "../../shared/AuthProvider";
import OwnOfferDetails from "./OwnOfferDetails";

type CurrentOwnOfferProps={
    o:OwnOfferType,
    offerLength:number,
    keyIndex:number,
}

export default function CurrentOwnOffer(props:CurrentOwnOfferProps){
    const {currentLang, currentCountry} = useContext(AuthContext);
    return (
        <>
            <ListItem alignItems="flex-start">
                <ListItemAvatar sx={{maxWidth:"20%"}}>
                    <Typography
                        sx={{ display: 'inline-block',fontWeight:"bold", lineHeight:"1.28rem" }}
                        component="span"
                        variant="subtitle1"
                        color="primary"
                    >
                        {new Date(props.o.shoppingDay!).getDay()}<br/>
                        {getShortMonthString(props.o.shoppingDay!)}<br/>
                        {new Date(props.o.shoppingDay!).getFullYear()}
                    </Typography>
                </ListItemAvatar>

                <ListItemText
                    primary={props.o.shopname!+" – " +props.o.shopAddress!.city}
                    secondary={
                        <React.Fragment>
                            {addressToString(props.o.shopAddress!)}<br/>
                            {addressToString(props.o.destinationAddress!)}<br/>
                            <Typography
                                sx={{ display: 'block',fontWeight:"bold",fontSize:"1rem", marginTop:".7rem" }}
                                component="span"
                                variant="body2"
                                color="text.primary"
                            >
                            {timeFromInstant(props.o.timeFrom!,currentLang+"-"+currentCountry)} – {timeFromInstant(props.o.timeTo!,currentLang+"-"+currentCountry)} Uhr
                            </Typography>
                            <Button sx={{color:"text.primary", paddingLeft:"0"}} variant={"text"} size={"small"}>Stornieren</Button>
                            <OwnOfferDetails  data={props.o}/>
                        </React.Fragment>
                    }
                />
            </ListItem>
            {!((props.offerLength-1)===props.keyIndex) &&
                <Divider variant="inset" component="li" sx={{marginRight:"1.2rem"}}/>
            }
        </>
    )
}
