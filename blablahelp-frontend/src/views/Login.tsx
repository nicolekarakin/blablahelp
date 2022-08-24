import {useSnackbar} from "notistack";
import React, {FormEvent, useContext, useState} from "react";

import {Button, Card, CardContent, CardHeader, Container, Grid, TextField} from "@mui/material";
import {useNavigate} from "react-router";
import axios from "axios";
import {AuthContext} from "../shared/AuthProvider";
import userType from "../types/UserType";
import {urls} from "../shared/UrlMapping";


export default function Login() {
    const {setCurrentUser} = useContext(AuthContext);

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const navigate = useNavigate();
    const {enqueueSnackbar} = useSnackbar();


    const handleLogin = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        const authdata = {auth: {username: username, password: password}}
        axios
            .get(urls.PUBLIC[0] + '/login', authdata)
            .then(response => {
                const user: userType = response?.data
                setCurrentUser(user);
                setUsername("");
                setPassword("");
                navigate("/account")
            })
            .catch(_ => {
                enqueueSnackbar('Login Failed', {variant: "error"})
            });
    }

    return (
        <Container maxWidth={false} sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",

        }}>
            <Container maxWidth={"sm"}>
                <Card>
                    <CardHeader title={"Login"}/>
                    <CardContent>
                        <form onSubmit={handleLogin}>
                            <Grid container
                                  display={"flex"}
                                  direction={"column"}
                                  spacing={2}
                            >
                                <Grid item>
                                    <TextField
                                        variant="outlined"
                                        placeholder={"Username"}
                                        value={username ?? ""}
                                        autoComplete={"username"}
                                        onChange={e => setUsername(e.target.value)}
                                        fullWidth
                                        error={false}
                                        label="Email"
                                        helperText="Incorrect email."
                                    />
                                </Grid>
                                <Grid item>
                                    <TextField
                                        variant="outlined"
                                        placeholder={"Password"}
                                        type={"password"}
                                        value={password ?? ""}
                                        autoComplete={"current-password"}
                                        onChange={e => setPassword(e.target.value)}
                                        fullWidth
                                        error={false}
                                        label="Password"
                                        helperText="Incorrect entry."
                                    />
                                </Grid>
                                <Grid item>
                                    <Button
                                        variant={"contained"}
                                        color={"primary"}
                                        type="submit"
                                    >
                                        Login
                                    </Button>
                                </Grid>
                            </Grid>
                        </form>
                    </CardContent>
                </Card>
            </Container>
        </Container>
    );
}
