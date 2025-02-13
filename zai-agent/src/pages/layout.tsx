import { Outlet } from "react-router-dom";

export default function MainLayout() {

    // const startLayout = useUserStore((state) => state.startLayout);

    // if (!startLayout) {
    //     return <SplashPage />
    // }

    return (
        <div className="bg-background">
            <Outlet />
        </div>
    );
}
