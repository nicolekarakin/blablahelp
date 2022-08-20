import React, {useContext} from 'react';
import {SnackbarProvider} from 'notistack';
import {Container} from "@mui/material";
import {BrowserRouter} from "react-router-dom";
import {Route, Routes} from "react-router";
import Login from "./views/Login";
import UserHome from "./views/UserHome";
import PublicHome from "./views/PublicHome";
import {AuthContext} from "./shared/AuthProvider";
import MyNavBar from "./components/MyNavBar";
import theme from "./hooks/useTheme";


const App = () => {
    const {currentUser} = useContext(AuthContext);
    console.log("currentUser: " + currentUser)
    return (
            <SnackbarProvider maxSnack={3} autoHideDuration={1000} preventDuplicate={true} hideIconVariant>
                <BrowserRouter>
                    <MyNavBar/>

                    <Container maxWidth={'md'} disableGutters
                               sx={{
                                   height: "100vh",
                                   padding: '.7em',
                                   bgcolor: theme.palette.grey[100]
                               }}>
                        <Routes>
                            {(!currentUser) ? <Route path="/" element={<PublicHome/>}/> : <></>
                            }
                            <Route path="/home" element={<UserHome/>}/>
                            <Route path="/login" element={<Login/>}/>

                        </Routes>
                        <h2>Hi {currentUser?.firstname}</h2>
                    </Container>
                </BrowserRouter>
            </SnackbarProvider>
    );
}
export default App;
