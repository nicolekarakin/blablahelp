import {Card, CardActions, CardContent, Rating, Typography} from "@mui/material";
import {capitalise, dateFromInstant} from "../../shared/util";
import {useContext} from "react";
import {AuthContext} from "../../shared/AuthProvider";

type CurrentPublicOfferProp = {
    name: string,
    date: number,
    motto: string,
    shopname:string,
    city:string,
    rating:number,
}
export default function CurrentPublicOffer(props: CurrentPublicOfferProp) {
    const {currentLang, currentCountry} = useContext(AuthContext);
    const locale=currentLang+"-"+currentCountry

    return (

        <Card sx={{minWidth: 275, marginBottom:'1.3rem'}} >
            <CardContent>
                <Typography sx={{fontSize: ".9rem"}} color="text.secondary" gutterBottom>
                    {props.shopname} – Lebensmitteleinkauf ist geplannt
                </Typography>
                <Typography variant="h5" component="div">
                    {capitalise(props.name)}
                </Typography>
                <Typography sx={{mb: 1.5}} color="text.secondary">
                    {props.city}, {dateFromInstant(props.date, locale)}
                </Typography>
                <Typography variant="body2">
                    {props?.motto}
                </Typography>
            </CardContent>
            <CardActions>
                <Rating name="read-only" value={props.rating} readOnly />
            </CardActions>
        </Card>
    )
}
