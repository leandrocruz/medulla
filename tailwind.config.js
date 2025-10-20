const defaultTheme = require('tailwindcss/defaultTheme')
export default {
    content: [
      "./index.html",
      "./modules/framework/target/**/*.js",
      "./modules/sample/target/**/*.js",
    ],
    theme: {
        extend: {
            fontFamily: {
                sans: ['Roboto', 'Inter var', ...defaultTheme.fontFamily.sans],
            },
            gridTemplateColumns: {
                DEFAULT: '250px auto',
                'mobile': '70px auto',
            },
            gridTemplateRows: {
                DEFAULT: '56px auto',
            },
            padding: {
                DEFAULT: '24px'
            },
        },
    },
}