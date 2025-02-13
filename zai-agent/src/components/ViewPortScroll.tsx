
interface Props {
    elementId: string;
}

const ViewPortScroll: React.FC<Props> = ({elementId}) => {
    const [tgReady] = useState(false)

    useEffect(() => {

        const scrollableEl = document.getElementById(elementId)

        // WebApp.expand()

        const overflow = 100

        let ts: number | undefined
        const onTouchStart = (e: TouchEvent) => {
            ts = e.touches[0].clientY
        }
        const onTouchMove = (e: TouchEvent) => {
            if (scrollableEl) {
                const scroll = scrollableEl.scrollTop
                const te = e.changedTouches[0].clientY
                console.log(scroll)
                if (scroll <= 0 && ts! < te) {
                    e.preventDefault()
                }
            } else {
                e.preventDefault()
            }
        }
        document.documentElement.addEventListener('touchstart', onTouchStart, { passive: false })
        document.documentElement.addEventListener('touchmove', onTouchMove, { passive: false })

        const onScroll = () => {
            console.log("onScroll")
            if (window.scrollY < overflow) {
                window.scrollTo(0, overflow)
                if (scrollableEl) {
                    scrollableEl.scrollTo(0, 0)
                }
            }
        }
        window.addEventListener('scroll', onScroll, { passive: true })

        // authorize here

        return () => {
            document.documentElement.removeEventListener('touchstart', onTouchStart)
            document.documentElement.removeEventListener('touchmove', onTouchMove)
            window.removeEventListener('scroll', onScroll)
        }

    }, [tgReady])

    return <></>
}

export default ViewPortScroll