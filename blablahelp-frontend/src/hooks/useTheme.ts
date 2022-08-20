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
        body1: {
            fontWeight: '400',
        },
        allVariants: {
            wordWrap: 'break-word',
        },

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
        MuiAppBar: {
            styleOverrides: {
                // Name of the slot
                root: {
                    '.MuiButton-text.MuiButton-root.active': {
                        textDecoration: 'underline'
                    },
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                // Name of the slot
                root: {
                    fontSize: '1rem',
                    fontFamily: 'Oswald',
                    fontWeight: '400',
                },
            },
        },
        MuiTypography: {
            styleOverrides: {
                h1: {
                    color: '#1402ff',
                    fontSize: '3rem',
                    fontWeight: '600',
                    lineHeight: '1.8'
                },
                h2: {
                    color: '#1402ff',
                    fontSize: '2.5rem',
                    fontWeight: '600',
                    lineHeight: '1.6'
                },
                h3: {
                    color: '#1402ff',
                    fontSize: '2rem',
                    fontWeight: '600',
                    lineHeight: '1.4'
                },
                h4: {
                    color: '#1402ff',
                    fontSize: '1.5rem',
                    fontWeight: '600',
                    lineHeight: '1.4'
                },
                h5: {
                    fontSize: '1.4rem',
                    fontWeight: '600',
                },
                h6: {
                    fontWeight: '600',
                    lineHeight: '1.4',
                },
                root: {
                    "a": {
                        fontFamily: 'Oswald',
                    },
                    "b": {
                        fontWeight: '600',
                    },
                }
            }
        }
    },
});

export default theme;