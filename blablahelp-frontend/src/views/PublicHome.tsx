import {Button, Card, CardActions, CardContent, Stack, Typography} from "@mui/material"

const PublicHome = () => {
    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >
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
        </Stack>
    )
}
export default PublicHome