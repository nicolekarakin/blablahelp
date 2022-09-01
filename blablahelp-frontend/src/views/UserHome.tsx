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

function UserHome() {
    const [userData, setUserData] = useState<string>()
    const [loading, setLoading] = useState(false);
    const {currentUser,setCurrentUser} = useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const navigate = useNavigate();


    const getPrivateHomeData = (id: string) => {
        return axios.get(urls.BASIC[0])
            .then(response => response.data)
            .then(data => setUserData(data))
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

    useEffect(() => {
        if (!currentUser) navigate("/login")
        else {
            setLoading(true);
            getPrivateHomeData(currentUser.id)
                .finally(() => setLoading(false));
            getUserData(currentUser.id);
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
                    {loading && (
                        <CircularProgress/>
                    )}

                    {!!currentUser && (
                        <Box>
                            <Alert>
                                <AlertTitle>Hello {currentUser.firstname}!</AlertTitle>
                                Willkommen zurück!
                            </Alert>

                            {!!userData && (
                                <Typography mt={3}>{userData}</Typography>
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

        </Stack>

    );
}

export default UserHome;
