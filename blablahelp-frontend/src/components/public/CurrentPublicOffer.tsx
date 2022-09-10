import {Card, CardActions, CardContent, Rating, Typography} from "@mui/material";
import {capitalise, dateFromInstant} from "../../shared/util";
import {useContext} from "react";
import {AuthContext} from "../../shared/AuthProvider";
import CurrentPublicOfferType from "../../types/CurrentPublicOfferType";


export default function CurrentPublicOffer(props: CurrentPublicOfferType) {
    const {currentLang, currentCountry} = useContext(AuthContext);
    const locale = currentLang + "-" + currentCountry

    return (

        <Card sx={{minWidth: 275, marginBottom: '1.3rem'}}>
            <CardContent>
                <Typography sx={{fontSize: ".9rem"}} color="text.secondary" gutterBottom>
                    {props.shopname} – Lebensmitteleinkauf ist geplannt
                </Typography>
                <Typography variant="h5" component="div">
                    {capitalise(props.firstname)}
                </Typography>
                <Typography sx={{mb: 1.5}} color="text.secondary">
                    {props.shopCity}, {dateFromInstant(props.shoppingDay, locale)}
                </Typography>

                <Typography sx={{fontSize: "1rem", fontWeight:"bold",mb: 1.5}} color="text.primary">
                    Eingekauft – {props.shoppingCount}<br/>
                    Dabei Storniert – {props.shoppingCancellation}<br/>
                </Typography>

                <Typography variant="body2">{props?.motto}</Typography>
            </CardContent>
            <CardActions>
                <Rating name="read-only" value={props.shoppingRating} readOnly />
            </CardActions>
        </Card>
    )
}
