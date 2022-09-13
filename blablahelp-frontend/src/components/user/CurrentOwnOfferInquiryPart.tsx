import {Box, Collapse, Divider, IconButton, IconButtonProps, Typography} from "@mui/material";
import React, {useContext} from "react";

import {addressToString, capitalise} from "../../shared/util";
import {AuthContext} from "../../shared/AuthProvider";
import FavoriteIcon from '@mui/icons-material/Favorite';
import MessageRoundedIcon from '@mui/icons-material/MessageRounded';
import FastForwardRoundedIcon from '@mui/icons-material/FastForwardRounded';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {styled} from '@mui/material/styles';
import ShoppingList from "./ShoppingList";

import OwnInquiryType from "../../types/OwnInquiryType";

type CurrentOwnOfferInquiryPartProps = {
    i: OwnInquiryType,
    handelKontakt: () => void,
}

interface ExpandMoreProps extends IconButtonProps {
    expand: boolean;
}

const ExpandMore = styled((props: ExpandMoreProps) => {
    const {expand, ...other} = props;
    return <IconButton {...other} />;
})(({theme, expand}) => ({
    transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
    }),
}));

export default function CurrentOwnOfferInquiryPart(props: CurrentOwnOfferInquiryPartProps) {
    const {currentUser} = useContext(AuthContext);
    const [expanded, setExpanded] = React.useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };


    return (
        <Box>
            <Divider variant="fullWidth" component="div"
                     sx={{
                         mb: ".2rem", mt: ".3rem", mr: ".2rem", borderColor: "#000000c4",
                         // borderColor:(theme) => theme.palette.secondary.main,
                         borderBottomWidth: ".1rem"
                     }}/>
            <Typography color="text.primary" component="span"
                        sx={{fontWeight: "bold", fontSize: "1rem", marginTop: ".7rem"}}>
                <FastForwardRoundedIcon
                    sx={{position: "absolute", mt: "0.2rem", left: "1rem", fontSize: "1.8rem"}}/>
                {capitalise(props.i.mitshopperFirstname)}
            </Typography>
            <IconButton aria-label="add to favorites" disabled>
                <FavoriteIcon/>
            </IconButton>
            <ShoppingList data={props.i.shoppingList}
                          shopperFirstname={capitalise(currentUser.firstname)}
                          mitshopperFirstname={capitalise(props.i.mitshopperFirstname)}/>
            <IconButton aria-label="chat" disabled>
                <MessageRoundedIcon/>
            </IconButton>
            <ExpandMore
                expand={expanded}
                onClick={handleExpandClick}
                aria-expanded={expanded}
                aria-label="show more"
            >
                <ExpandMoreIcon/>
            </ExpandMore>
            <Collapse in={expanded} timeout="auto" unmountOnExit
                      key={props.i.mitshopperAccountId}>

                <Typography sx={{display: 'block', mb: 0.5}} color="text.secondary"
                            component="span">
                    {addressToString(props.i.mitshopperAddress!)}
                </Typography>

                <Typography component="span"
                            sx={{display: 'block', fontSize: "0.875rem", lineHeight: 1.43}}>
                    {props.i.notes}
                </Typography>


            </Collapse>
        </Box>

    )
}
