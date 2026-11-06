// Título padrão das páginas: título, barrinha vermelha embaixo e subtítulo (opcional)
function CabecalhoPagina({ titulo, subtitulo }) {
  return (
    <div className="cabecalho-pagina">
      <h1>{titulo}</h1>
      <span className="barra-vermelha" />
      {subtitulo && <p className="texto-suave">{subtitulo}</p>}
    </div>
  )
}

export default CabecalhoPagina
