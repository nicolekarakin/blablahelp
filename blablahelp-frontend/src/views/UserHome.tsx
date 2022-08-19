import {useSnackbar} from "notistack";
import {useContext, useEffect, useState} from "react";
import {Person} from "@mui/icons-material";
import {useNavigate} from "react-router";

import {Alert, AlertTitle, Box, Card, CardContent, CircularProgress, Container, Grid,} from "@mui/material";
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
                enqueueSnackbar("Fetching user data failed!", {variant: "error"});
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
        <Container maxWidth={false} disableGutters
                   sx={{
                       root: {
                           height: "100%"
                       },
                       grid: {
                           height: "100%"
                       },
                       logoutBtn: {
                           marginLeft: "auto"
                       }
                   }}
        >

            <Grid container
                  justifyContent={"center"}
                  alignContent={"center"}
                  spacing={3}
            >
                <Grid item xs={6}>
                    <Card>
                        <CardContent>
                            {loading && (
                                <CircularProgress/>
                            )}

                            {!!currentUser && (
                                <Box
                                    display="flex"
                                    alignItems={"center"}
                                    flexDirection="column"
                                >
                                    <Person/>
                                    <Box width={0.5}>
                                        <Alert>
                                            <AlertTitle>Hello {currentUser.firstname}!</AlertTitle>
                                            Welcome back!
                                        </Alert>
                                    </Box>
                                    {!!userData && (
                                        <Box width={0.5}>{userData}</Box>
                                    )}
                                </Box>
                            )}
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </Container>
    );
}

export default UserHome;