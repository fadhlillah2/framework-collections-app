import React, { useEffect, useState } from 'react';
import './Portfolio.css';

function Portfolio() {
  const [portfolios, setPortfolios] = useState([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editName, setEditName] = useState('');
  const [editDescription, setEditDescription] = useState('');
  const [error, setError] = useState(null);
  const [filterText, setFilterText] = useState('');  // New state for filter text

  useEffect(() => {
    fetch('/portfolios')
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch portfolios');
        return response.json();
      })
      .then(data => setPortfolios(data))
      .catch(error => setError(error.message));
  }, []);

  const addPortfolio = async () => {
    const response = await fetch('http://localhost:8080/portfolios', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, description }),
    });
  
    if (!response.ok) {
      throw new Error('Failed to add portfolio');
    }
  
    const newPortfolio = await response.json();
    setPortfolios(prevPortfolios => [...prevPortfolios, newPortfolio]);
    setName('');
    setDescription('');
  };

  const updatePortfolio = async () => {
    const response = await fetch(`http://localhost:8080/portfolios/${editingId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: editName, description: editDescription }),
    });

    if (!response.ok) {
      throw new Error('Failed to update portfolio');
    }

    const updatedPortfolio = await response.json();
    setPortfolios(prevPortfolios => prevPortfolios.map(portfolio => portfolio.id === editingId ? updatedPortfolio : portfolio));
    setEditName('');
    setEditDescription('');
    setEditingId(null);
  };
  
  const deletePortfolio = async (id) => {
    const response = await fetch(`http://localhost:8080/portfolios/${id}`, {
      method: 'DELETE',
    });
  
    if (!response.ok) {
      throw new Error('Failed to delete portfolio');
    }
  
    setPortfolios(prevPortfolios => prevPortfolios.filter(portfolio => portfolio.id !== id));
  };

  const clearFilter = () => setFilterText('');  // New function to clear filter

  const filteredPortfolios = portfolios.filter(portfolio => portfolio.name.toLowerCase().includes(filterText.toLowerCase()));  // Filter portfolios


  return (
    <div className="portfolio-container">
      
      <h1>Portfolios</h1>

      {error && <div className="error-message">{error}</div>}

      <div>
        <input type="text" value={filterText} onChange={e => setFilterText(e.target.value)} placeholder="Filter by name" />
        <button onClick={clearFilter}>Clear filter</button>  {/* Clear filter button */}
      </div>

      <table className="portfolio-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {filteredPortfolios.map((portfolio) => (
          <tr key={portfolio.id}>
            <td>{portfolio.name}</td>
            <td>{portfolio.description}</td>
            <td>
            <button onClick={() => { setEditName(portfolio.name); setEditDescription(portfolio.description); setEditingId(portfolio.id); }}>Edit</button>
                <button onClick={() => deletePortfolio(portfolio.id)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>

      <div className="portfolio-form">
        <h2>Add Portfolio</h2>
        <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Name" />
        <input type="text" value={description} onChange={e => setDescription(e.target.value)} placeholder="Description" />
        <button onClick={addPortfolio}>Add portfolio</button>
      </div>

      {editingId && <div className="portfolio-form">
        <h2>Edit Portfolio</h2>
        <input type="text" value={editName} onChange={e => setEditName(e.target.value)} placeholder="Name" />
        <input type="text" value={editDescription} onChange={e => setEditDescription(e.target.value)} placeholder="Description" />
        <button onClick={updatePortfolio}>Update portfolio</button>
      </div>}

    </div>
  );
}

export default Portfolio;
