import {Box, Stack, Typography} from "@mui/material"

const How = () => {

    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >
            <Box ml={2} mr={2} mt={3}>
                <Typography variant={'h1'}>Wie?</Typography>

                <Typography component={'p'} mb={'1.2rem'}>
                    Copy muss noch geschrieben werden..
                </Typography>

            </Box>

        </Stack>
    )
}
export default How
