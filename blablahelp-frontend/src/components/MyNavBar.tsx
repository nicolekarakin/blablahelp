import {AppBar, Box, Button, Container, IconButton, Menu, MenuItem, Toolbar, Tooltip, Typography} from "@mui/material";
import {Menu as MenuIcon} from "@mui/icons-material";
import React, {useContext} from "react";
import {AuthContext} from "../shared/AuthProvider";
import {useSnackbar} from "notistack";
import {useNavigate} from "react-router";
import axios from "axios";
import {urls} from "../shared/UrlMapping";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';


const pages = ['Home', 'About', 'AGB', 'Kontakt'];
const settings = ['Profile', 'Aktuell', 'Abgelaufen', 'History', 'Shoppinglists'];


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


    const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
    const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

    const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    return (
        <AppBar position="static" sx={{maxWidth: '900px', margin: 'auto'}}>
            <Container maxWidth="md" sx={{justifyContent: 'center', color: 'white'}}>
                <Box
                    component="img"
                    sx={{
                        height: '1.5em',
                        position: 'absolute',
                    }}
                    alt="Logo"
                    src="logo192.png"
                />
                <Toolbar disableGutters>
                    <Box sx={{flexGrow: 1, display: {xs: 'flex', md: 'none'}}}>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleOpenNavMenu}
                            color="inherit"
                        >
                            <MenuIcon/>
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorElNav}
                            anchorOrigin={{
                                vertical: 'bottom',
                                horizontal: 'left',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'left',
                            }}
                            open={Boolean(anchorElNav)}
                            onClose={handleCloseNavMenu}
                            sx={{
                                display: {xs: 'block', md: 'none'},
                            }}
                        >
                            {pages.map((page) => (
                                <MenuItem key={page} onClick={handleCloseNavMenu}>
                                    <Typography textAlign="center" sx={{
                                        fontSize: '1rem',
                                        fontFamily: 'Oswald',
                                        fontWeight: '400'
                                    }}>{page}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>

                    <Typography
                        variant="h6"
                        noWrap
                        component="a"
                        href="/"
                        sx={{
                            mr: 2,
                            display: {xs: 'none', md: 'flex'},
                            fontWeight: 600,
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        BlaBlaHelp
                    </Typography>

                    <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                        {pages.map((page) => (
                            <Button
                                key={page}
                                onClick={handleCloseNavMenu}
                                sx={{my: 2, color: 'white', display: 'block'}}
                            >
                                {page}
                            </Button>
                        ))}
                    </Box>

                    {(currentUser) ?
                        <>
                            <Button color="inherit" onClick={handleLogout}>Logout</Button>
                            <Box sx={{flexGrow: 0}}>
                                <Tooltip title="Open settings">
                                    <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                                        <AccountCircleIcon sx={{color: 'white', fontSize: 30}}/>
                                    </IconButton>
                                </Tooltip>
                                <Menu
                                    sx={{mt: '45px'}}
                                    id="menu-appbar"
                                    anchorEl={anchorElUser}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    keepMounted
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={Boolean(anchorElUser)}
                                    onClose={handleCloseUserMenu}
                                >
                                    {settings.map((setting) => (
                                        <MenuItem key={setting} onClick={handleCloseUserMenu}>
                                            <Typography textAlign="center" sx={{
                                                fontFamily: 'Oswald',
                                                fontWeight: 400
                                            }}>{setting}</Typography>
                                        </MenuItem>
                                    ))}
                                </Menu>
                            </Box>
                        </>
                        : <Button color="inherit" onClick={() => navigate("/login")}>Login</Button>
                    }


                </Toolbar>
            </Container>
        </AppBar>
    )
}