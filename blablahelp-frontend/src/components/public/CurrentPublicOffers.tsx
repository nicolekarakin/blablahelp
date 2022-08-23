import {Box, Typography} from "@mui/material";
import CurrentPublicOffer from "./CurrentPublicOffer";

const fakeData=[
    {
    name: "Agneschka",
    date: 1663022076,
    motto: "well meaning and kindly",
    shopname:"Lidl",
    city:"Ulm",
    rating:4,
},
    {
        name: "Bernd",
        date: 1663454491,
        motto: "Jede Woche eine neue Welt",
        shopname:"Lidl",
        city:"Hannover",
        rating:2,
    },
]

export default function CurrentPublicOffers(){

    return (
        <Box>
            <Typography variant={'h1'} ml={2} mr={2}>
                Aktuelle Angebote
            </Typography>
            { fakeData.map(a=>(<CurrentPublicOffer {...a}/>))}
        </Box>
    )
}
