import {Card, CardActions, CardContent, Rating, Typography} from "@mui/material";
import {capitalise, dateFromInstant} from "../../shared/util";

type CurrentPublicOfferProp = {
    name: string,
    date: number,
    motto: string,
    shopname:string,
    city:string,
    rating:number,
}
export default function CurrentPublicOffer(props: CurrentPublicOfferProp) {

    return (

        <Card sx={{minWidth: 275, marginBottom:'1.3rem'}} >
            <CardContent>
                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                    {props.shopname} – Lebensmitteleinkauf ist geplannt
                </Typography>
                <Typography variant="h5" component="div">
                    {capitalise(props.name)}
                </Typography>
                <Typography sx={{mb: 1.5}} color="text.secondary">
                    {props.city}, {dateFromInstant(props.date)}
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
