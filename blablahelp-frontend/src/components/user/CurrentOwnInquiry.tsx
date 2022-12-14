import {Button, Divider, IconButton, ListItem, ListItemText, Typography} from "@mui/material";
import React, {useContext} from "react";

import {addressToString, capitalise, timeFromInstant} from "../../shared/util";
import {AuthContext} from "../../shared/AuthProvider";
import NotOwnOfferDetails from "./NotOwnOfferDetails";

import {useSnackbar} from "notistack";
import {OwnInquiryResponseType} from "../../types/OwnInquiryType";
import ShoppingList from "./ShoppingList";
import ListAvatarForDates from "./ListAvatarForDates";
import axios from "axios";
import {urls} from "../../shared/UrlMapping";
import userType from "../../types/UserType";
import MessageRoundedIcon from "@mui/icons-material/MessageRounded";
import FavoriteIcon from "@mui/icons-material/Favorite";

type CurrentOwnInquiryProps = {
    d: OwnInquiryResponseType,
    offerLength: number,
    keyIndex: number,
}

export default function CurrentOwnInquiry(props: CurrentOwnInquiryProps) {
    const {currentUser, setCurrentUser, currentLang, currentCountry} = useContext(AuthContext);
    const locale = currentLang + "-" + currentCountry
    const {enqueueSnackbar} = useSnackbar();

    const handelDelete = (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault()

        axios
            .delete(urls.BASIC[0] + urls.BASIC[2] + "/" + currentUser.id + urls.BASIC[6] + "/" + props.d.offer.offerId)
            .then(_ => {
                const updatedOwnInquiries: OwnInquiryResponseType[] = currentUser?.currentInquiries
                    .filter((a: OwnInquiryResponseType) => a.inquiry.offerId !== props.d.offer.offerId
                    )

                setCurrentUser((currentUser: userType) => ({
                    ...currentUser, currentInquiries: updatedOwnInquiries
                }));

            })
            .catch(e => {
                enqueueSnackbar('Inquiry Deletion Failed', {variant: "error"})
            });
    }


    return (
        <>
            {(props.d && props.d.offer && props.d.inquiry) &&
                <ListItem alignItems="flex-start">
                    <ListAvatarForDates shoppingDay={props.d.offer.shoppingDay} locale={locale}/>
                    <ListItemText disableTypography={true}>
                        <Typography component="h5">
                            {props.d.offer.shopname + " ??? Shopping mit " + capitalise(props.d.offer.firstname)}
                        </Typography>
                        <Typography component="p" variant={"body2"} color={"text.secondary"}>
                            {addressToString(props.d.offer.shopAddress)}<br/>
                            {addressToString(props.d.inquiry.mitshopperAddress!)}<br/>
                        </Typography>
                        <Typography
                            sx={{display: 'block', fontWeight: "bold", fontSize: "1rem", marginTop: ".7rem"}}
                            component="span"
                            variant="body2"
                            color="text.primary"
                        >
                            {timeFromInstant(props.d.offer.timeFrom, locale)} ??? {timeFromInstant(props.d.offer.timeTo, locale)} Uhr
                        </Typography>

                        <Button sx={{color: "text.primary", paddingLeft: "0"}}
                                variant={"text"} size={"small"} onClick={handelDelete}>Stornieren</Button>
                        <NotOwnOfferDetails data={props.d.offer}/>
                        <ShoppingList data={props.d.inquiry.shoppingList}
                                      shopperFirstname={capitalise(props.d.offer.firstname)}
                                      mitshopperFirstname={capitalise(currentUser.firstname)}/>

                        <IconButton aria-label="add to favorites" disabled>
                            <FavoriteIcon/>
                        </IconButton>
                        <IconButton aria-label="chat" disabled>
                            <MessageRoundedIcon/>
                        </IconButton>

                        <Typography component="span"
                                    sx={{display: 'block', fontSize: "0.875rem", lineHeight: 1.43}}>
                            {props.d.offer.notes}
                        </Typography>

                    </ListItemText>
                </ListItem>
            }
            {((props.offerLength - 1) !== props.keyIndex) &&
                <Divider variant="fullWidth" component="div"
                         sx={{
                             marginRight: "1.2rem",
                             borderColor: (theme) => theme.palette.primary.main,
                             borderBottomWidth: ".26rem"
                         }}/>
            }
        </>

    )
}
