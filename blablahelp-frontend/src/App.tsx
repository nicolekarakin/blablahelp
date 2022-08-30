import React from 'react';
import {SnackbarProvider} from 'notistack';
import {Container} from "@mui/material";
import {Route, Routes} from "react-router";
import Login from "./views/Login";
import UserHome from "./views/UserHome";
import PublicHome from "./views/PublicHome";
import MyNavBar from "./components/MyNavBar";
import theme from "./hooks/useTheme";
import Agb from "./views/public/Agb";
import Impressum from "./views/public/Impressum";
import Profile from "./views/user/Profile";
import How from "./views/public/How";
import OfferForm from "./views/user/OfferForm";


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
                    <Route path="/wie" element={<How/>}/>

                    <Route path="/account" element={<UserHome/>}/>
                    <Route path="/profile" element={<Profile/>}/>
                    <Route path="/newOffer" element={<OfferForm/>}/>
                </Routes>

            </Container>

        </SnackbarProvider>
    );
}
export default App;
