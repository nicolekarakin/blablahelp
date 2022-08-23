import React from 'react';
import {SnackbarProvider} from 'notistack';
import {Container} from "@mui/material";
import {Route, Routes} from "react-router";
import Login from "./views/Login";
import UserHome from "./views/UserHome";
import PublicHome from "./views/PublicHome";
import MyNavBar from "./components/MyNavBar";
import theme from "./hooks/useTheme";
import Agb from "./views/Agb";
import Impressum from "./views/Impressum";
import Profile from "./views/Profile";


const App = () => {

    return (
        <SnackbarProvider maxSnack={3} autoHideDuration={1000} preventDuplicate={true} hideIconVariant>

            <MyNavBar/>

            <Container maxWidth={'md'} disableGutters
                       sx={{
                           minHeight: "100vh",
                           padding: '.7em',
                           bgcolor: theme.palette.grey[100]
                       }}>

                <Routes>

                    <Route path="/" element={<PublicHome/>}/>
                    <Route path="/login" element={<Login/>}/>

                    <Route path="/agb" element={<Agb/>}/>
                    <Route path="/impressum" element={<Impressum/>}/>

                    <Route path="/home" element={<UserHome/>}/>
                    <Route path="/profile" element={<Profile/>}/>
                </Routes>

            </Container>

        </SnackbarProvider>
    );
}
export default App;
