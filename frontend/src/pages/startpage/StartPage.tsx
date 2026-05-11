import background from "@assets/background-placeholder.png";
import { useEffect, useState } from "react";

const VALID_CODE = "12345"; // Beispiel-Code

function StartPage() {
	const [code, setCode] = useState("");
	const [error, setError] = useState(false);

	const handleJoin = () => {
		if (code.trim() === VALID_CODE) {
			setError(false);
			window.alert("Login!");
			// TODO: Weiterleiten / Spiel beitreten
		} else {
			window.alert("Login Faild!");
			setError(true);
		}
	};

	const handleCreate = () => {
		// TODO: Weiterleiten / Spiel Erstellen
		window.alert("Create new Game!");
	};

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setCode(e.target.value);
		if (error) setError(false);
	};

	// noch etwas suboptimal (lint fehler)
	useEffect(() => {
		if (code.length >= 5) {
			handleJoin();
		}
	}, [code]);

	return (
		<div
			className="w-screen h-screen bg-cover bg-center flex items-center"
			style={{ backgroundImage: `url(${background})` }}
		>
			<div className="flex items-center px-16 w-full">
				{/* Linke Box: Input oder Fehlermeldung */}
				<div className="max-w-sm w-80">
					{error ? (
						<div className="bg-red-400/80 border-2 border-red-500/40 rounded-xl px-8 py-5 text-center">
							<p className="text-red-900 font-medium text-base">
								Der Code ist nicht aktiv..
							</p>
						</div>
					) : (
						<input
							type="text"
							value={code}
							onChange={handleChange}
							onKeyDown={(e) => e.key === "Enter" && handleJoin()}
							placeholder="Spiel Code eingeben..."
							className="w-full bg-white/90 rounded-xl px-6 py-4 text-gray-500 text-base outline-none border-2 border-transparent focus:border-amber-700/40 transition-colors placeholder:text-gray-400"
						/>
					)}
				</div>

				{/* Trennlinie */}
				<div className="w-px h-32 bg-amber-950/60 mx-8" />

				{/* Rechte Seite: Button */}
				<div>
					<button
						type="button"
						onClick={handleCreate}
						className="bg-amber-900 hover:bg-amber-800 active:scale-95 text-amber-100 font-medium text-base px-8 py-3 rounded-lg transition-all"
					>
						Spiel erstellen
					</button>
				</div>
			</div>
		</div>
	);
}

export default StartPage;
