import {Box, Divider, Typography} from "@mui/material";
import {ProductType} from "../../types/ShoppingListType";
import React from "react";

export default function Product(props: ProductType) {
    return (
        <Box>
            <Typography variant="h6" color={"primary"}>{props.title}</Typography>
            <Typography color="text.secondary">
                {props.amount + " " + props.unit}
            </Typography>
            <Typography sx={{fontSize: "1rem", fontWeight: "bold", mb: 1.5}} color="text.primary">
                {props.category.join(", ")}
            </Typography>
            <Divider variant="fullWidth" sx={{marginRight: "1.2rem"}}/>
        </Box>
    )
}