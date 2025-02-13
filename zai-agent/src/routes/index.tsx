import ZAIAgent from "@/pages/zaiagent";
// const Friends = lazy(() => import("@/pages/friends"))
import MainLayout from "@/pages/layout";
import { createHashRouter, RouterProvider } from "react-router-dom"

const _asyncComponent = () => {
    throw new Promise(() => { });
}

const routes = [
    {
        path: '/',
        element: <MainLayout />,
        children: [
            {
                path: '',
                element: <ZAIAgent />,
            },

            // {
            //     path: 'exchange',
            //     element: <Exchange />,
            // }
        ]
    }, {
        path: '*',
        element: <Navigate to="/" />
    }
]

const router = createHashRouter(routes);
const Routes = () => {
    return <RouterProvider router={router} />
}


export default Routes;