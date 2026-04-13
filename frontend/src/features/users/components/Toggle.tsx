type Props = {
  checked: boolean;
  onChange: (val: boolean) => void;
};

const Toggle = ({ checked, onChange }: Props) => {
  return (
    <div
      onClick={() => onChange(!checked)}
      className={`w-10 h-5 flex items-center rounded-full p-1 cursor-pointer transition ${
        checked ? "bg-green-500" : "bg-gray-300"
      }`}
    >
      <div
        className={`bg-white w-4 h-4 rounded-full shadow-md transform transition ${
          checked ? "translate-x-5" : ""
        }`}
      />
    </div>
  );
};

export default Toggle;