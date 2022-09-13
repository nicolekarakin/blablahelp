import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {Link as RouterLink} from 'react-router-dom';
import {Alert, AlertTitle, Box, Button, Card, CardContent, CircularProgress, Stack, Typography,} from "@mui/material";
import {AuthContext} from "../shared/AuthProvider";
import CurrentOwnOfferList from "../components/user/CurrentOwnOfferList";
import CurrentOwnInquiryList from "../components/user/CurrentOwnInquiryList";
import useUserHome from "../hooks/useUserHome";
import {capitalise} from "../shared/util";

function UserHome() {
    const [userMessage, setUserMessage] = useState<string>()
    const [loading, setLoading] = useState(false);
    const {currentUser} = useContext(AuthContext);

    const navigate = useNavigate();

    const {getUserOffers, getUserData, getPrivateHomeData, getUserInquiries} = useUserHome()

    useEffect(() => {
        if (!currentUser) navigate("/login")
        else {
            getPrivateHomeData(setUserMessage);

            if (!currentUser.userData || !currentUser.userData.accountId) {
                setLoading(true);
                getUserData()
                    .then((_: any) => {
                        getUserOffers()
                            .then((_: any) => {
                                getUserInquiries()
                                    .finally(() => setLoading(false))
                            })

                    })
            } else if (!currentUser.userOffers && !currentUser.userInquiries) {
                setLoading(true);
                getUserOffers()
                    .then((_: any) => {
                        getUserInquiries()
                            .finally(() => setLoading(false))
                    })
            } else if (!currentUser.userOffers) {
                setLoading(true);
                getUserOffers().finally(() => setLoading(false))
            } else if (!currentUser.userInquiries) {
                setLoading(true);
                getUserInquiries().finally(() => setLoading(false))
            }

        }
    }, []);


    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >

            <Card elevation={3}>
                <CardContent>

                    {!!currentUser && (
                        <Box>
                            <Alert>
                                <AlertTitle>Hallo {capitalise(currentUser.firstname)}</AlertTitle>
                                Willkommen zurück!
                            </Alert>

                            {!!userMessage && (
                                <Typography mt={3}>{userMessage}</Typography>
                            )}
                        </Box>
                    )}
                </CardContent>
            </Card>

            <Button
                variant="contained"
                component={RouterLink} to={"/newOffer"}
            >Neues Angebot erstellen
            </Button>
            <Button
                variant="contained"
                component={RouterLink} to={"/offerSearch"}
            >Nach einem Angebot suchen
            </Button>

            {loading && (
                <CircularProgress/>
            )}

            {(!!currentUser && (currentUser?.currentOffers?.length > 0)) && (
                <CurrentOwnOfferList offers={currentUser.currentOffers}/>
            )}

            {(!!currentUser && (currentUser?.currentInquiries?.length > 0)) && (
                <CurrentOwnInquiryList inquiries={currentUser.currentInquiries}/>
            )}

        </Stack>

    );
}

export default UserHome;
