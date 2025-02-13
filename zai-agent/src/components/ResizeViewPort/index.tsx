import style1 from "./index.module.css";

const ResizeViewPort = () => {
    const [tgReady] = useState(false)

    useEffect(() => {

        // const scrollableEl = document.getElementById(elementId)

        // WebApp.expand()

        const overflow = 100
        function setupDocument(enable: boolean) {
            if (enable) {
                document.documentElement.classList.add(style1.html)
                document.body.style.marginTop = `${overflow}px`
                document.body.style.height = window.innerHeight + overflow + "px"
                document.body.style.paddingBottom = `${overflow}px`
                window.scrollTo(0, overflow)
            } else {
                document.documentElement.classList.remove(style1.html)
                document.body.style.removeProperty('marginTop')
                document.body.style.removeProperty('height')
                document.body.style.removeProperty('paddingBottom')
                window.scrollTo(0, 0)
            }
        }
        setupDocument(true)

        // let ts: number | undefined
        // const onTouchStart = (e: TouchEvent) => {
        //     ts = e.touches[0].clientY
        // }
        // const onTouchMove = (e: TouchEvent) => {
        //     if (scrollableEl) {
        //         const scroll = scrollableEl.scrollTop
        //         const te = e.changedTouches[0].clientY
        //         console.log(scroll)
        //         if (scroll <= 0 && ts! < te) {
        //             e.preventDefault()
        //         }
        //     } else {
        //         e.preventDefault()
        //     }
        // }
        // document.documentElement.addEventListener('touchstart', onTouchStart, { passive: false })
        // document.documentElement.addEventListener('touchmove', onTouchMove, { passive: false })
        //
        // const onScroll = () => {
        //     console.log("onScroll")
        //     if (window.scrollY < overflow) {
        //         window.scrollTo(0, overflow)
        //         if (scrollableEl) {
        //             scrollableEl.scrollTo(0, 0)
        //         }
        //     }
        // }
        // window.addEventListener('scroll', onScroll, { passive: true })

        // authorize here

        return () => {
            setupDocument(false)
            // document.documentElement.removeEventListener('touchstart', onTouchStart)
            // document.documentElement.removeEventListener('touchmove', onTouchMove)
            // window.removeEventListener('scroll', onScroll)
        }


        // document.body.classList.add(style1.body_m)
        // document.getElementById('warp')?.classList.add(style1.warp_m)
        // document.getElementById('root')?.classList.add(style1.root_m)


    }, [tgReady])

    return <></>
}

export default ResizeViewPort