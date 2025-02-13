import Routes from "@/routes"
import { Suspense } from 'react'
import CustomToast from "@/components/CustomToast.tsx";
import WalletContext from "@/pages/wallet/WalletContext.tsx";
import {DialogProvider} from "@/components/GlobalDialogContext.tsx";
// import ResizeViewPort from "@/components/ResizeViewPort";
// import {resizeViewPort} from "@/utils/utils.ts";

function App() {

  console.log('App');

  useEffect(() => {
    console.log("Build Time:", process.env.VITE_BUILD_TIME);
  }, []);

  return (
    <>
      <Suspense fallback={<p>Loading...</p>}>
        {/* <ResizeViewPort /> */}
        <WalletContext>
          <DialogProvider>
              <Routes />
          </DialogProvider>
        </WalletContext>
        <CustomToast/>
      </Suspense>
    </>
  )
}

export default App
