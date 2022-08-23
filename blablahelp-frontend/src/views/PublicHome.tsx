import {Box, Button, Card, CardActions, CardContent, Stack, Typography} from "@mui/material"
import {Link} from "react-router-dom";

const cardList = (
    <Box>
        <Typography variant={'h1'} ml={2} mr={2}>
            Aktuelle Angebote
        </Typography>
        <Card sx={{minWidth: 275}}>
            <CardContent>
                <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                    Public Word of the Day
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
    </Box>
)

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
            {cardList}
        </Stack>
    )
}
export default PublicHome