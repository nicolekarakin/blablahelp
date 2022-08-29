import {Box, Stack, Typography} from "@mui/material"
import {Link} from "react-router-dom";
import CurrentPublicOffers from "../components/public/CurrentPublicOffers";


const PublicHome = () => {

    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >
            <Box ml={2} mr={2} mt={3}>
                <Typography variant={'h1'}>
                    BlaBlaHelp?
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    BlaBlaHelp App bringt Menschen, die Hilfe beim Lebenseinkauf brauchen, und Menschen, die helfen
                    wollen, zusammen.
                    Registriere dich, und je nach Situation, kannst du Hauptshopper oder Mitschopper sein!
                </Typography>
                <Typography component={'p'}>
                    Als Shopper, kannst du dein Angebot veröffentlichen. Als Mitschopper, gib deine Adresse ein und
                    finde jemanden, der dir mit Einkaufen hilft.
                    Wie genau es funktioniert und was du beachten solltest, kannst du in <Link
                    to={'/agb'}>"AGB"</Link> und <Link to={'/wie'}>"Wie?"</Link> Seiten nachlesen.
                </Typography>
            </Box>
            <CurrentPublicOffers />
        </Stack>
    )
}
export default PublicHome
