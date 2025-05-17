import ButtonGroup from "../comp/ButtonGroup";
import { API_ROUTES } from "../comp/config";

const buttons = (navigateRecord, submitForm) => [
  {
    key: "new",
    title: "Nuevo Registro",
    icon: "/img/New-48.png",
    onClick: () => navigateRecord(API_ROUTES.NEW_RECORD),
  },
  {
    key: "save",
    title: "Guardar",
    icon: "/img/Save-48.png",
    onClick: submitForm,
  },
  {
    key: "cancel",
    title: "Cancelar",
    icon: "/img/UndoIcon01.png",
    onClick: () => navigateRecord(API_ROUTES.CURRENT_RECORD),
  },
  {
    key: "first",
    title: "Primer Registro",
    icon: "/img/Home-btn.png",
    onClick: () => navigateRecord(API_ROUTES.FIRST_RECORD),
  },
  {
    key: "prev",
    title: "Anterior",
    icon: "/img/Arrow-Left-Blue-48.png",
    onClick: () => navigateRecord(API_ROUTES.PREV_RECORD),
  },
  {
    key: "next",
    title: "Siguiente",
    icon: "/img/Arrow-Right-Blue-48.png",
    onClick: () => navigateRecord(API_ROUTES.NEXT_RECORD),
  },
];
