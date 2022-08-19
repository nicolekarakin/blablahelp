import {AppBar, Button, IconButton, Toolbar, Typography} from "@mui/material";
import {Menu as MenuIcon} from "@mui/icons-material";
import React, {useContext} from "react";
import {AuthContext} from "../shared/AuthProvider";
import {useSnackbar} from "notistack";
import {useNavigate} from "react-router";
import axios from "axios";
import {NavLink} from "react-router-dom";
import {urls} from "../shared/UrlMapping";

export default function MyNavBar() {
    const {currentUser, setCurrentUser} = useContext(AuthContext);
    const navigate = useNavigate();
    const {enqueueSnackbar} = useSnackbar();
    const handleLogout = () => {
        axios.get(urls.BASIC[0] + '/logout')
            .then(response => response.data)
            .then(_ => {
                setCurrentUser(null)
                navigate("/")
            })
            .catch(_ => {
                enqueueSnackbar("Logout failed!", {variant: "error"})
            })
        ;
    }

    return (
        <AppBar position="static">
            <Toolbar>
                <IconButton edge="start" color="inherit" aria-label="menu">
                    <MenuIcon/>
                </IconButton>
                <Typography variant="h6">
                    <NavLink to={"/"}>BlaBlaHelp</NavLink>
                </Typography>
                {(currentUser) ?
                    <Button color="inherit" onClick={handleLogout}>Logout</Button>
                    : <Button color="inherit" onClick={() => navigate("/login")}>Login</Button>
                }
            </Toolbar>
        </AppBar>
    )
}