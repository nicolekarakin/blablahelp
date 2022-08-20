import {createTheme} from "@mui/material";

const theme = createTheme({
    palette: {
        primary: {
            main: "#ff8401",
        }
    },
    typography: {
        fontFamily: [
            'Nunito',
            'sans-serif',
        ].join(','),
        fontWeightRegular: 400,
        fontWeightMedium: 600,
    },
    // breakpoints: {
    //     values: {
    //         mobile: 0,
    //         tablet: 640,
    //         laptop: 1024,
    //         desktop: 1280,
    //     },
    // },
    components: {
        MuiCard: {
            styleOverrides: {
                root: {
                    borderRadius: '6px',
                }
            }
        },
        MuiButton: {
            styleOverrides: {
                // Name of the slot
                root: {
                    fontSize: '1rem',
                    fontFamily: 'Oswald',
                    fontWeight: '400'
                },
            },
        },
    },
});

export default theme;