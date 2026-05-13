import background from "@assets/background-placeholder.png";
import { useState } from "react";
import { useNavigate } from "react-router";

function StartPage() {
	const navigate = useNavigate();
	const [name, setName] = useState("");

	const handleNext = () => {
		navigate("/startpage");
	};
	const _changeName = (e: React.ChangeEvent<HTMLInputElement>) => {
		setName(e.target.value);
	};

	return (
		<div
			className="w-screen h-screen bg-cover bg-center flex items-center"
			style={{ backgroundImage: `url(${background})` }}
		></div>
	);
}

export default StartPage;
