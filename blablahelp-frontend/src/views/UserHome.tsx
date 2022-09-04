import {useSnackbar} from "notistack";
import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {Link as RouterLink} from 'react-router-dom';
import {
    Alert,
    AlertTitle,
    Box, Button,
    Card,
    CardContent,
    CircularProgress,
    Stack,
    Typography,
} from "@mui/material";
import axios from "axios";
import {AuthContext} from "../shared/AuthProvider";
import {urls} from "../shared/UrlMapping";
import CurrentOwnOfferList from "../components/user/CurrentOwnOfferList";
import {UserDataType} from "../types/UserType";

function UserHome() {
    const [userMessage, setUserMessage] = useState<string>()
    const [loading, setLoading] = useState(false);
    const {currentUser,setCurrentUser} = useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const navigate = useNavigate();


    const getPrivateHomeData = (id: string) => {
        return axios.get(urls.BASIC[0])
            .then(response => response.data)
            .then(data => setUserMessage(data))
            .catch(_ => {
                enqueueSnackbar("Fetching private home data for user with id "+id+" failed!", {variant: "error"});
            });
    }

    const getUserData = (id: string) => {
        return axios.get(urls.BASIC[0]+urls.BASIC[2]+"/"+id)
            .then(response => response.data)
            .then(data => {
                setCurrentUser({...currentUser, userData: data})
            })
            .catch(_ => {
                enqueueSnackbar("Fetching data for user with id "+id+" failed!", {variant: "error"});
            });
    }

    const getUserOffers = (id: string) => {
        return axios.get(urls.BASIC[0]+urls.BASIC[2]+"/"+id+"/"+urls.BASIC[1])
            .then(response => response.data)
            .then(data => {
                const userDataUpdated:UserDataType={...currentUser.userData, currentOffers: data }
                setCurrentUser({...currentUser, userData: userDataUpdated})
            })
            .catch(_ => {
                enqueueSnackbar("Fetching data for user with id "+id+" failed!", {variant: "error"});
            });
    }

    useEffect(() => {
        if (!currentUser) navigate("/login")
        else {
            getPrivateHomeData(currentUser.id);

            if(!currentUser.userData || !currentUser.userData.accountId) {
                setLoading(true);
                getUserData(currentUser.id).then(_ => getUserOffers(currentUser.id)).finally(() => setLoading(false))
            }
            else if(currentUser.userData && !currentUser.userData.userOffers) {
                setLoading(true);
                getUserOffers(currentUser.id).finally(() => setLoading(false))
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
                                <AlertTitle>Hello {currentUser.firstname}!</AlertTitle>
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

            {loading && (
                <CircularProgress/>
            )}

            {(!!currentUser && ( currentUser?.userData?.currentOffers?.length > 0)) &&(
                <CurrentOwnOfferList offers={currentUser.userData?.currentOffers}/>
            )}

        </Stack>

    );
}

export default UserHome;
