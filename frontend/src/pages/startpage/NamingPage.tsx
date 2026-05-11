import background from "@assets/background-placeholder.png";
import {useState } from "react";
import {useNavigate} from "react-router";


function StartPage() {
    const navigate = useNavigate()
    const [name, setName] = useState("");

    const handleNext = () => {
        navigate("/startpage")
    };
    const changeName = (e: React.ChangeEvent<HTMLInputElement>) => {

        setName(e.target.value);
    }





    return (
        <div
            className="w-screen h-screen bg-cover bg-center flex items-center"
            style={{ backgroundImage: `url(${background})` }}
        >
            <div className="flex items-center justify-center w-1/2 h-full">
                {/* Linke Box: Input oder Fehlermeldung */}
                    <input
                        type="text"
                        value={name}
                        onChange={changeName}
                        onKeyDown={(e) => e.key === "Enter" && handleNext()}
                        placeholder="Name eingeben:"
                        className="w-80 bg-white/90 rounded-xl px-6 py-4 text-gray-500 text-base outline-none border-2 border-transparent focus:border-amber-700/40 transition-colors placeholder:text-gray-400"
                    />
            </div>

        </div>
    );
}

export default StartPage;
