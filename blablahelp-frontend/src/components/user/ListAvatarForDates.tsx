import {Box, ListItemAvatar, Typography} from "@mui/material";
import {getShortMonthString, weekdayFromInstant} from "../../shared/util";
import React from "react";

export default function ListAvatarForDates(props: { shoppingDay: number, locale: string }) {

    return (
        <ListItemAvatar sx={{maxWidth: "20%"}}>
            <Typography
                sx={{display: 'inline-block', fontWeight: "bold", lineHeight: "1.28rem"}}
                component="span"
                variant="subtitle1"
                color="primary"
            >
                {new Date(props.shoppingDay!).getDate()}<br/>
                {getShortMonthString(props.shoppingDay!, props.locale)}<br/>
                {new Date(props.shoppingDay!).getFullYear()}<br/>
                <Box component={"span"} sx={{color: "text.primary", display: "inline-block", marginTop: ".8rem"}}>
                    {weekdayFromInstant(props.shoppingDay!, props.locale)}
                </Box>
            </Typography>
        </ListItemAvatar>
    )
}