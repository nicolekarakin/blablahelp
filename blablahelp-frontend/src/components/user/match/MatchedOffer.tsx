import {Box, Card, CardActions, CardContent, Rating, Stack, Typography} from "@mui/material";

import {useContext} from "react";
import NotOwnOfferType from "../../../types/NotOwnOfferType";
import {AuthContext} from "../../../shared/AuthProvider";
import {addressToString, capitalise, dateFromInstant, timeFromInstant} from "../../../shared/util";
import InquiryDialog from "./InquiryDialog";


export default function MatchedOffer(props: NotOwnOfferType) {
    const {currentLang, currentCountry} = useContext(AuthContext);
    const locale = currentLang + "-" + currentCountry

    const getNamePriceLine = () => {
        const str = capitalise(props.firstname)
        if (parseInt(props.priceOffer) > 0)
            return str + " (" + props.priceOffer + " Euro)"
        return str
    }
    const handleSubmitInquiry = (shopListTitle: string) => {
        console.debug("handleShow, offerId: " + props.offerId
            + ", shopAddr: " + props.shopAddress
            + ", shopListTitle: " + shopListTitle)
    }
    return (

        <Card sx={{minWidth: 275, marginBottom: '1.3rem'}}>
            <CardContent>
                <Typography sx={{fontSize: ".9rem"}} color="text.secondary" gutterBottom>
                    {props.shopname + " – " + addressToString(props.shopAddress)}
                </Typography>

                <Typography variant="h5" component="div" sx={{mt: 1.3}}>
                    <Stack direction={{xs: 'column', sm: 'row'}}
                           justifyContent="space-between"
                           alignItems="baseline"
                           spacing={{xs: 1, sm: 2, md: 4}}>
                        {getNamePriceLine()}
                        <Rating name="read-only" value={props.shoppingRating} readOnly/>
                    </Stack>
                </Typography>
                <Typography component={"div"} sx={{fontSize: "1rem", fontWeight: "bold", mb: 1.5}} color="text.primary">
                    <Stack direction={{xs: 'column', sm: 'row'}}
                           justifyContent="space-between"
                           alignItems="baseline"
                           spacing={{xs: 1, sm: 2, md: 4}}>
                        <Typography variant="body2" color="text.secondary">Eingekauft: {props.shoppingCount} |
                            Storniert: {props.shoppingCancellation}</Typography>
                        <Box component={"span"}>Reviews</Box>
                    </Stack>

                </Typography>

                <Typography variant="h6" color={"primary"}>
                    {dateFromInstant(props.shoppingDay, locale)}<br/>
                    {timeFromInstant(props.timeFrom!, currentLang + "-" + currentCountry) + " und " + timeFromInstant(props.timeTo!, currentLang + "-" + currentCountry)} Uhr
                </Typography>

                <Typography sx={{mb: 1.5, mt: 1.5}} color="text.secondary">
                    Maximal {props.maxArticles} Produktartikel
                    <Box component={"span"} sx={{fontSize: ".8rem"}}> (ohne Flüssigkeiten)</Box><br/>
                    Maximal an Flüssigkeiten {props.maxLiter!} Liter<br/>
                </Typography>
                {
                    !(props.notes && props.notes.length > 0) &&
                    <Typography sx={{fontSize: ".9rem", mb: 1.5}} color="text.secondary" gutterBottom>
                        Anmerkungen: The new search appearance filter is named "translated results" and it shows you how
                        many searchers accessed your site's content when Google
                    </Typography>
                }

                <Typography variant="body2">{props?.motto}</Typography>
            </CardContent>
            <CardActions sx={{mb: '1rem'}}>
                <InquiryDialog handleSubmit={handleSubmitInquiry}/>
            </CardActions>
        </Card>
    )
}
