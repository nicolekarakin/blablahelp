import {Box, Stack, Typography} from "@mui/material"

const Impressum = () => {

    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >
            <Box ml={2} mr={2} mt={3}>
                <Typography variant={'h1'}>Impressum</Typography>

                <Typography component={'p'} mb={'1.2rem'}>
                    BlaBlaHelp App<br/>
                    Anschrift?? XX<br/>
                    E-Mail: info-blablahelp@gmail.de
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    Redaktionell verantwortlich nach § 55 Abs.2 RStV:<br/>
                    XXX, Anschrift wie oben
                </Typography>

            </Box>

        </Stack>
    )
}
export default Impressum