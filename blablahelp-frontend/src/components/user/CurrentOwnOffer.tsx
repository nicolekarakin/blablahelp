import {Box, Button, Divider, ListItem, ListItemAvatar, ListItemText, Typography} from "@mui/material";
import React, {useContext} from "react";
import OwnOfferType from "../../types/OwnOfferType";
import {
    addressToString,
    getShortMonthString,
    timeFromInstant,
    weekdayFromInstant
} from "../../shared/util";
import {AuthContext} from "../../shared/AuthProvider";
import OwnOfferDetails from "./OwnOfferDetails";
import axios from "axios";
import {urls} from "../../shared/UrlMapping";
import userType, {UserDataType} from "../../types/UserType";
import {useSnackbar} from "notistack";

type CurrentOwnOfferProps={
    o:OwnOfferType,
    offerLength:number,
    keyIndex:number,
}

export default function CurrentOwnOffer(props:CurrentOwnOfferProps){
    const {currentUser, setCurrentUser,currentLang, currentCountry} = useContext(AuthContext);
    const locale=currentLang+"-"+currentCountry
    const {enqueueSnackbar} = useSnackbar();
    const handelDelete=()=>{
        axios
            .delete(urls.BASIC[0]+urls.BASIC[2]+"/"+currentUser.id+urls.BASIC[1]+"/"+props.o.offerId)
            .then(_ => {
                const updatedOwnOffers:OwnOfferType[]=currentUser?.userData?.currentOffers.filter((a:OwnOfferType)=>a.offerId!==props.o.offerId)
                const updatedUserData:UserDataType={...currentUser?.userData, currentOffers:updatedOwnOffers}
                setCurrentUser((currentUser:userType) => ({
                    ...currentUser, userData: updatedUserData
                }));

            })
            .catch(_ => {
                enqueueSnackbar('Offer Deletion Failed', {variant: "error"})
            });
    }
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
                        {new Date(props.o.shoppingDay!).getDate()}<br/>
                        {getShortMonthString(props.o.shoppingDay!,locale)}<br/>
                        {new Date(props.o.shoppingDay!).getFullYear()}<br/>
                        <Box component={"span"} sx={{color:"text.primary",display: "inline-block",marginTop:".8rem"}}>
                            {weekdayFromInstant(props.o.shoppingDay!,locale)}
                        </Box>
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
                            {timeFromInstant(props.o.timeFrom!,locale)} – {timeFromInstant(props.o.timeTo!,locale)} Uhr
                            </Typography>
                            <Button sx={{color:"text.primary", paddingLeft:"0"}} variant={"text"} size={"small"} onClick={handelDelete}>Stornieren</Button>
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
