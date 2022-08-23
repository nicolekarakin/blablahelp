import {Box, Stack, Typography} from "@mui/material"
import {useContext} from "react";
import {AuthContext} from "../shared/AuthProvider";
import {capitalise} from "../shared/util";
import {Navigate} from "react-router-dom";


const Profile = () => {
    const {currentUser} = useContext(AuthContext);

    if (currentUser === null) return (
        <Navigate to={"/login"}/>
    )
    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >
            <Box ml={2} mr={2} mt={3}>
                <Typography variant={'h1'}>{capitalise(currentUser.firstname)}</Typography>

                <Typography component={'p'} mb={'1.2rem'}>
                    {currentUser.firstname} Name<br/>
                    Anschrift: RollenStr. 89, Poli<br/>
                    E-Mail: yyxx@gmail.de
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    And More
                </Typography>

            </Box>

        </Stack>
    )
}
export default Profile