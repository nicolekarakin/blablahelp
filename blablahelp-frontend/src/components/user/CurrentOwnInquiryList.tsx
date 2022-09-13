import {Box, List, Typography} from "@mui/material";
import React, {useContext} from "react";
import {OwnInquiryResponseType} from "../../types/OwnInquiryType";
import CurrentOwnInquiry from "./CurrentOwnInquiry";
import {AuthContext} from "../../shared/AuthProvider";

type CurrentOwnInquiryListProps = {
    inquiries: OwnInquiryResponseType[],
}
export default function CurrentOwnInquiryList(props: CurrentOwnInquiryListProps) {
    const {currentUser} = useContext(AuthContext);
    if (currentUser.currentInquiries && currentUser.currentInquiries.length > 0) {
        return (
            <Box>
                <Typography variant={'h1'} ml={2} mr={2}>
                    Mitshopping
                </Typography>
                <List sx={{width: '100%', bgcolor: 'background.paper', paddingBottom: "1rem"}}>
                    {
                        props.inquiries.map((a, index) => (
                            <CurrentOwnInquiry key={index} d={a} offerLength={props.inquiries.length} keyIndex={index}/>
                        ))
                    }
                </List>
            </Box>
        )
    } else return <></>
}
