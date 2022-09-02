import {createTheme, Theme} from "@mui/material";



const defaultTheme = createTheme()
const { breakpoints, typography: { pxToRem } } = defaultTheme


const theme:Theme = createTheme({
    ...defaultTheme,
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
    //         xs: 0,
    //         sm: 640,
    //         md: 900,
    //         lg: 1280,
    //         xl: 1536,
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
                    [breakpoints.only("xs")]: {
                        fontSize: "2rem"
                    },
                    fontWeight: '600',
                    lineHeight: '1.8'
                },
                h2: {
                    color: '#1402ff',
                    fontSize: '2.5rem',
                    [breakpoints.only("xs")]: {
                        fontSize: "1.7rem"
                    },
                    fontWeight: '600',
                    lineHeight: '1.6'
                },
                h3: {
                    color: '#1402ff',
                    fontSize: '2rem',
                    [breakpoints.only("xs")]: {
                        fontSize: "1.5rem"
                    },
                    fontWeight: '600',
                    lineHeight: '1.4'
                },
                h4: {
                    color: '#1402ff',
                    fontSize: '1.5rem',
                    [breakpoints.only("xs")]: {
                        fontSize: "1.3rem"
                    },
                    fontWeight: '600',
                    lineHeight: '1.4'
                },
                h5: {
                    fontSize: '1.4rem',
                    [breakpoints.only("xs")]: {
                        fontSize: "1.2rem"
                    },
                    fontWeight: '600',
                },
                h6: {
                    fontWeight: '600',
                    lineHeight: '1.4',
                    [breakpoints.only("xs")]: {
                        fontSize: "1.1rem"
                    },
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
