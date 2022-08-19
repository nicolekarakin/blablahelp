import React, {useContext} from 'react';
import {SnackbarProvider} from 'notistack';
import {Container, ThemeProvider} from "@mui/material";
import {BrowserRouter} from "react-router-dom";
import {Route, Routes} from "react-router";
import Login from "./views/Login";
import theme from "./hooks/useTheme";
import UserHome from "./views/UserHome";
import PublicHome from "./views/PublicHome";
import {AuthContext} from "./shared/AuthProvider";
import MyNavBar from "./components/MyNavBar";


const App = () => {
    const {currentUser} = useContext(AuthContext);

    return (
        <ThemeProvider theme={theme}>
            <SnackbarProvider maxSnack={3} autoHideDuration={1000} preventDuplicate={true} hideIconVariant>
                <BrowserRouter>
                    <MyNavBar/>

                    <Container maxWidth={false} disableGutters
                               sx={{height: "100vh", bgcolor: theme.palette.grey[500]}}>
                        <Routes>
                            {(!currentUser) ? <Route path="/" element={<PublicHome/>}/> : <></>
                            }
                            <Route path="/home" element={<UserHome/>}/>
                            <Route path="/login" element={<Login/>}/>

                        </Routes>
                    </Container>
                </BrowserRouter>
            </SnackbarProvider>
        </ThemeProvider>
    );
}
export default App;
