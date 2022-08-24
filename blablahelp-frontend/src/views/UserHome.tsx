import {useSnackbar} from "notistack";
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router";

import {
    Alert,
    AlertTitle,
    Box,
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
    const {currentUser} = useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();
    const navigate = useNavigate();


    const getUserData = (id: string) => {
        return axios.get(urls.BASIC[0])
            .then(response => response.data)
            .then(data => setUserData(data))
            .catch(_ => {
                enqueueSnackbar("Fetching data for user with id "+id+" failed!", {variant: "error"});
            });
    }


    useEffect(() => {
        if (!currentUser) navigate("/login")
        else {
            setLoading(true);
            getUserData(currentUser.id)
                .finally(() => setLoading(false));
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
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

        </Stack>

    );
}

export default UserHome;
