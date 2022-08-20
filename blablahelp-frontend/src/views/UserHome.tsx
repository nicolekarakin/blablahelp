import {useSnackbar} from "notistack";
import {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router";

import {
    Alert,
    AlertTitle,
    Box,
    Button,
    Card,
    CardActions,
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
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >

            <Card sx={{minWidth: 275}}>
                <CardContent>
                    <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                        Word of the Day
                    </Typography>
                    <Typography variant="h5" component="div">
                        benevolent
                    </Typography>
                    <Typography sx={{mb: 1.5}} color="text.secondary">
                        adjective
                    </Typography>
                    <Typography variant="body2">
                        well meaning and kindly.
                        <br/>
                        {'"a benevolent smile"'}
                    </Typography>
                </CardContent>
                <CardActions>
                    <Button size="small">Learn More</Button>
                </CardActions>
            </Card>

            <Card elevation={3}>
                <CardContent>
                    {loading && (
                        <CircularProgress/>
                    )}

                    {!!currentUser && (
                        <Box>
                            <Alert>
                                <AlertTitle>Hello {currentUser.firstname}!</AlertTitle>
                                Welcome back!
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