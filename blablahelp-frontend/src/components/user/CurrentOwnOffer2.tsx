import {Button, Divider, ListItem, ListItemText, Typography} from "@mui/material";
import React, {useContext} from "react";
import OwnOfferType from "../../types/OwnOfferType";
import {addressToString, timeFromInstant} from "../../shared/util";
import {AuthContext} from "../../shared/AuthProvider";
import OwnOfferDetails from "./OwnOfferDetails";
import axios from "axios";
import {urls} from "../../shared/UrlMapping";
import userType from "../../types/UserType";
import {useSnackbar} from "notistack";
import ListAvatarForDates from "./ListAvatarForDates";
import CurrentOwnOfferInquiryPart from "./CurrentOwnOfferInquiryPart";

type CurrentOwnOfferProps = {
    o: OwnOfferType,
    offerLength: number,
    keyIndex: number,
}

export default function CurrentOwnOffer(props: CurrentOwnOfferProps) {
    const {currentUser, setCurrentUser, currentLang, currentCountry} = useContext(AuthContext);
    const locale = currentLang + "-" + currentCountry
    const {enqueueSnackbar} = useSnackbar();


    const handelDelete = () => {
        axios
            .delete(urls.BASIC[0] + urls.BASIC[2] + "/" + currentUser.id + urls.BASIC[1] + "/" + props.o.offerId)
            .then(_ => {
                const updatedOwnOffers: OwnOfferType[] = currentUser?.currentOffers.filter((a: OwnOfferType) => a.offerId !== props.o.offerId)

                setCurrentUser((currentUser: userType) => ({
                    ...currentUser, currentOffers: updatedOwnOffers
                }));

            })
            .catch(e => {
                console.debug(e)
                enqueueSnackbar('Offer Deletion Failed', {variant: "error"})
            });
    }

    return (
        <>
            {(props.o && props.o.shopAddress) &&
                <ListItem alignItems="flex-start">
                    <ListAvatarForDates shoppingDay={props.o.shoppingDay!} locale={locale}/>

                    <ListItemText
                        primary={props.o.shopname! + " – " + props.o.shopAddress?.city}
                        secondary={
                            <React.Fragment>
                                {addressToString(props.o.shopAddress!)}<br/>
                                {addressToString(props.o.destinationAddress!)}<br/>
                                <Typography
                                    sx={{display: 'block', fontWeight: "bold", fontSize: "1rem", marginTop: ".7rem"}}
                                    component="span"
                                    variant="body2"
                                    color="text.primary"
                                >
                                    {timeFromInstant(props.o.timeFrom!, locale)} – {timeFromInstant(props.o.timeTo!, locale)} Uhr
                                </Typography>
                                <Button sx={{color: "text.primary", paddingLeft: "0"}} variant={"text"} size={"small"}
                                        onClick={handelDelete}>Stornieren</Button>
                                <OwnOfferDetails data={props.o}/>

                                {
                                    (props.o.inquiries && props.o.inquiries?.length > 0) &&
                                    props.o.inquiries.map(mit =>
                                            <CurrentOwnOfferInquiryPart key={mit.mitshopperAccountId}
                                                                        handelKontakt={handelDelete} i={mit}/>
                                        // <React.Fragment key={mit.mitshopperAccountId}>
                                        //     <Typography color="text.primary" component="span"
                                        //                 sx={{
                                        //                     display: 'block', fontWeight: "bold",
                                        //                     fontSize: "1rem", marginTop: ".7rem"
                                        //                 }}>
                                        //         {capitalise(mit.mitshopperFirstname)}
                                        //     </Typography>
                                        //     <Typography sx={{display: 'block', mb: 0.5}} color="text.secondary"
                                        //                 component="span">
                                        //         {addressToString(mit.mitshopperAddress!)}
                                        //     </Typography>
                                        //
                                        //     <Typography component="span"
                                        //                 sx={{display: 'block', fontSize: "0.875rem", lineHeight: 1.43}}>
                                        //         {mit.notes}
                                        //     </Typography>
                                        //     <ShoppingList data={mit.shoppingList}
                                        //                   shopperFirstname={capitalise(currentUser.firstname)}
                                        //                   mitshopperFirstname={capitalise(mit.mitshopperFirstname)}/>
                                        //
                                        //     <Button sx={{color: "text.primary", paddingLeft: "0"}} disabled={true}
                                        //             variant={"text"} size={"small"}
                                        //             onClick={handelKontakt}>Kontakt</Button>
                                        // </React.Fragment>
                                    )
                                }
                            </React.Fragment>
                        }

                    />

                </ListItem>
            }
            {((props.offerLength - 1) !== props.keyIndex) &&
                <Divider variant="inset" component="li" sx={{marginRight: "1.2rem"}}/>
            }
        </>
    )
}
