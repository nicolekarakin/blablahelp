import {Box, Card, CardActions, CardContent, Rating, Stack, Typography} from "@mui/material";

import {useContext} from "react";
import NotOwnOfferType from "../../../types/NotOwnOfferType";
import {AuthContext} from "../../../shared/AuthProvider";
import {addressToString, capitalise, dateFromInstant, timeFromInstant} from "../../../shared/util";
import InquiryDialog from "./InquiryDialog";
import axios from "axios";
import {urls} from "../../../shared/UrlMapping";
import OwnInquiryType, {OwnInquiryResponseType} from "../../../types/OwnInquiryType";
import {fakeShoppingList} from "./FakeShoppingList";
import {useSnackbar} from "notistack";
import {useNavigate} from "react-router";
import useUserHome from "../../../hooks/useUserHome";
import AddressType from "../../../types/AddressType";

type MatchedOfferProps = {
    mitshopperAddress: AddressType,
    selectedOffer: NotOwnOfferType,
}
export default function MatchedOffer(props: MatchedOfferProps) {
    const {currentLang, currentCountry, currentUser} = useContext(AuthContext);
    const {updateCurrentInquiries} = useUserHome()
    const locale = currentLang + "-" + currentCountry
    const {enqueueSnackbar} = useSnackbar();
    const navigate = useNavigate()

    const getNamePriceLine = () => {
        const str = capitalise(props.selectedOffer.firstname)
        if (parseInt(props.selectedOffer.priceOffer) > 0)
            return str + " (" + props.selectedOffer.priceOffer + " Euro)"
        return str
    }

    const createInquiry = () => {
        const inquiry: OwnInquiryType = {
            offerId: props.selectedOffer.offerId,
            mitshopperAccountId: currentUser.id,
            mitshopperFirstname: currentUser.firstname,
            mitshopperAddress: props.mitshopperAddress,
            inquiryPrice: "0",
            notes: "Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.",
            shoppingList: fakeShoppingList,
        }
        return inquiry
    }

    // const updateCurrentUserState = (ownInquiry:OwnInquiryResponse) => {
    //     const newState = currentUser.currentInquiries.map((obj: OwnInquiryResponse) => {
    //         if (obj.offer.offerId === ownInquiry.offer.offerId) {
    //             return {...ownInquiry};
    //         }
    //         return obj;
    //     });
    //     setCurrentUser(newState);
    //     console.debug(ownInquiry)
    // }

    const handleSubmitInquiry = (shopListTitle: string) => {
        const inquiry = createInquiry()
        inquiry.shoppingList.title = shopListTitle

        axios
            .post(urls.BASIC[0] + urls.BASIC[2] + urls.BASIC[5], inquiry)
            .then(response => {
                const ownInquiryData: OwnInquiryResponseType = response?.data
                updateCurrentInquiries(ownInquiryData)
            })
            .catch(_ => {
                enqueueSnackbar('New Inquiry Submission Failed', {variant: "error"})
            });
        navigate("/account")
    }

    return (

        <Card sx={{minWidth: 275, marginBottom: '1.3rem'}}>
            <CardContent>
                <Typography sx={{fontSize: ".9rem"}} color="text.secondary" gutterBottom>
                    {props.selectedOffer.shopname + " – " + addressToString(props.selectedOffer.shopAddress)}
                </Typography>

                <Typography variant="h5" component="div" sx={{mt: 1.3}}>
                    <Stack direction={{xs: 'column', sm: 'row'}}
                           justifyContent="space-between"
                           alignItems="baseline"
                           spacing={{xs: 1, sm: 2, md: 4}}>
                        {getNamePriceLine()}
                        <Rating name="read-only" value={props.selectedOffer.shoppingRating} readOnly/>
                    </Stack>
                </Typography>
                <Typography component={"div"} sx={{fontSize: "1rem", fontWeight: "bold", mb: 1.5}} color="text.primary">
                    <Stack direction={{xs: 'column', sm: 'row'}}
                           justifyContent="space-between"
                           alignItems="baseline"
                           spacing={{xs: 1, sm: 2, md: 4}}>
                        <Typography variant="body2"
                                    color="text.secondary">Eingekauft: {props.selectedOffer.shoppingCount} |
                            Storniert: {props.selectedOffer.shoppingCancellation}</Typography>
                        <Box component={"span"}>Reviews</Box>
                    </Stack>

                </Typography>

                <Typography variant="h6" color={"primary"}>
                    {dateFromInstant(props.selectedOffer.shoppingDay, locale)}<br/>
                    {timeFromInstant(props.selectedOffer.timeFrom!, currentLang + "-" + currentCountry) + " und " + timeFromInstant(props.selectedOffer.timeTo!, currentLang + "-" + currentCountry)} Uhr
                </Typography>

                <Typography sx={{mb: 1.5, mt: 1.5}} color="text.secondary">
                    Maximal {props.selectedOffer.maxArticles} Produktartikel
                    <Box component={"span"} sx={{fontSize: ".8rem"}}> (ohne Flüssigkeiten)</Box><br/>
                    Maximal an Flüssigkeiten {props.selectedOffer.maxLiter!} Liter<br/>
                </Typography>
                {
                    !(props.selectedOffer.notes && props.selectedOffer.notes.length > 0) &&
                    <Typography sx={{fontSize: ".9rem", mb: 1.5}} color="text.secondary" gutterBottom>
                        Anmerkungen: The new search appearance filter is named "translated results" and it shows you how
                        many searchers accessed your site's content when Google
                    </Typography>
                }

                <Typography variant="body2">{props?.selectedOffer.motto}</Typography>
            </CardContent>
            <CardActions sx={{mb: '1rem'}}>
                <InquiryDialog handleSubmit={handleSubmitInquiry}/>
            </CardActions>
        </Card>
    )
}
